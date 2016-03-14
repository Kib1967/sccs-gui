import java.nio.file.*
import org.ajoberstar.grgit.*

println "Entered process.groovy"

if( !session ) {
	session = request.getSession( true )
}
	
def model = session.getAttribute 'propertiesModel'
String propertiesFileName = context.getInitParameter( 'properties.file' )
	
// This shouldn't happen at this point
if( !model ) {

	println "Creating new properties model instance"

	Path path = Paths.get( localDir, propertiesFileName )
	model = new PropertiesModel()
	def reader = new PropertiesReader()
	reader.readAll( path.text, model )
	session.setAttribute 'propertiesModel', model
}

println "Got properties model instance"

def parameterNames = request.getParameterNames()

parameterNames.each { parameterName ->
	def value = request.getParameter( parameterName )
	
	def (propertyName, envName) = parameterName.tokenize( ':' )
	
	def valuesForProperty = model.getValuesForProperty( propertyName )
	if( valuesForProperty != null ) {
		valuesForProperty[ propertyName ] << [(envName):(value)]
	} else {
		println "No values for property ${propertyName}"
	}
}

println "Handled all properties"

String remoteRepo = context.getInitParameter( 'git.remote' )
String localDir = context.getInitParameter( 'git.local' )
String branchName = context.getInitParameter( 'git.branch' )
boolean cloneIfAbsent = context.getInitParameter( 'git.clone-if-absent' )
boolean autoPush = context.getInitParameter( 'git.auto-push' )

println "Got all init parameters"

Git git = new Git( remoteRepo, localDir, branchName, cloneIfAbsent, autoPush )
	
println "Constructed git instance"

new PropertiesWriter().writeAll( model, propertiesFileName )

println "Wrote all properties to file"

git.commit()

println "Committed to git"

//response.sendRedirect '/environments.groovy?r=success'