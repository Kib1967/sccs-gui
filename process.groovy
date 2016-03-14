if( !session ) {
	session = request.getSession( true )
}
	
def model = session.getAttribute 'propertiesModel'
String propertiesFileName = context.getInitParameter( 'properties.file' )
	
// This shouldn't happen at this point
if( !model ) {

	Path path = localDir.resolve( propertiesFileName )
	def model = new PropertiesModel()
	def reader = new PropertiesReader()
	reader.readAll( path.text, model )
	session.setAttribute 'propertiesModel', model
}

def parameterNames = request.getParameterNames()

parameterNames.each { parameterName ->
	def value = request.getParameter( parameterName )
	
	def (propertyName, envName) = parameterName.tokenize( ':' )
	
	model.valuesForProperty[propertyName] << [(envName):(value)]
}

String remoteRepo = context.getInitParameter( 'git.remote' )
String localDir = context.getInitParameter( 'git.local' )
String branchName = context.getInitParameter( 'git.branch' )
boolean cloneIfAbsent = context.getInitParameter( 'git.clone-if-absent' )
boolean autoPush = context.getInitParameter( 'git.auto-push' )

Git git = new Git( remoteRepo, localDir, branchName, cloneIfAbsent, autoPush )
	
new PropertiesWriter().writeAll( model, propertiesFileName )

git.commit()

response.sendRedirect '/environments.groovy?r=success'