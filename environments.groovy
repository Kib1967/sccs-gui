if( !session ) {
	session = request.getSession( true )
}
	
def allProperties = session.getAttribute 'allProperties'
	
if( !allProperties ) {
	String remoteRepo = context.getInitParameter( 'git.remote' )
	String localDir = context.getInitParameter( 'git.local' )
	String branchName = context.getInitParameter( 'git.branch' )
	boolean cloneIfAbsent = context.getInitParameter( 'git.clone-if-absent' )
	boolean autoPush = context.getInitParameter( 'git.auto-push' )
	String propertiesFileName = context.getInitParameter( 'properties.file' )
	
	Git git = new Git( remoteRepo, localDir, branchName, cloneIfAbsent, autoPush )

	Path path = localDir.resolve( propertiesFileName )
	def reader = new PropertiesReader()
	allProperties = reader.readAll( path.text )
	session.setAttribute 'allProperties', allProperties
}

def showSuccessBanner = 'success'.equals( request.getParameter( 'r' ))

html.html {
    head {
        title 'Properties'
		link(rel: 'stylesheet', href: './bootstrap.min.css', media: 'screen')
		link(rel: 'stylesheet', href: './environments.css', media: 'screen')
		script(type: 'text/javascript', src: './environments.js')
    }
    body {
		div(class: 'container') {
		
			h1 'Properties per environment'
			
			if( showSuccessBanner ) {
				div( class: 'panel panel-success' ) {
					div( class: 'panel-heading' ) {
						h3( class: 'panel-title' ) {
							mkp.yield( 'Success' )
						}
					}
					div( class: 'panel-body' ) {
						mkp.yield( 'Property edits were saved successfully' )
					}
				}
			}
			
			form( action: '/process.groovy', method: 'POST' ) {
				button(
					class: 'btn btn-primary',
					type: 'submit' ) {
					mkp.yield( 'Save all changes' )
				}
		
				table(class: 'table table-striped table-hover') {
					thead {
						tr {
							th 'Key'
							th 'Environment'
							th 'Value'
							th ''
						}
					}
					tbody {
						allProperties.each { key, submap ->
							def rowCount = submap.entrySet().size()
							submap.eachWithIndex { env, value, index ->
								tr {
									def divId = "${env}:${key}.div"
									def fieldId = "${env}:${key}"
									def displayableValue = (value==null) ? '[empty]' : value
									def editableValue = (value==null) ? '' : value
									
									if( index==0 ) {
										td('rowspan': rowCount) { mkp.yield(key) }
									}
									td env
									td {
										input(
											type: 'text',
											class: 'form-control editor-field',
											name: "${fieldId}",
											value: "${editableValue}")
									}
									td {
										div {
											button(
												class: 'btn btn-primary',
												type: 'submit',
												onclick: "revertField('${divId}', '${value}');") {
												mkp.yield('Revert')
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
