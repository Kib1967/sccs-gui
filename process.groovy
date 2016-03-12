if( !session ) {
	session = request.getSession( true )
}
	
def allProperties = session.getAttribute 'allProperties'
String propertiesFileName = context.getInitParameter( 'properties.file' )
	
// This shouldn't happen at this point
if( !allProperties ) {

	def reader = new PropertiesReader()
	allProperties = reader.readAll( propertiesFileName )
	session.setAttribute 'allProperties', allProperties
}

def parameterNames = request.getParameterNames()

parameterNames.each { parameterName ->
	def value = request.getParameter( parameterName )
	
	def (propertyName, envName) = parameterName.tokenize( ':' )
	
	allProperties[(envName)][(propertyName)] = value
}

String remoteRepo = context.getInitParameter( 'git.remote' )
String localDir = context.getInitParameter( 'git.local' )
String branchName = context.getInitParameter( 'git.branch' )
boolean cloneIfAbsent = context.getInitParameter( 'git.clone-if-absent' )
boolean autoPush = context.getInitParameter( 'git.auto-push' )

Git git = new Git( remoteRepo, localDir, branchName, cloneIfAbsent, autoPush )
	
new PropertiesWriter().writeAll( allProperties, propertiesFileName )

git.commit()

response.sendRedirect '/environments.groovy?r=success'