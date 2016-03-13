/**
	Handles the mapping between the YAML structure and the GUI structure.
	YAML representation is a stream with multiple documents, each keyed by an environment name.
	GUI structure is a map of property names to a map of values for that property in each environment.
	Internally, the latter structure is created; the mapping to the YAML model is done on request.
*/
class PropertiesModel {

	def environmentNames = []
	def properties = [:]
	
	def addEnvironment( String environmentName, Map environmentProperties ) {
		if( environmentNames.contains( environmentName )) {
			throw new IllegalArgumentException( "Environment ${environmentName} already exists in this model instance" )
		}
		environmentProperties.each { propertyName, value ->
			def existingValues = properties[(propertyName)]
			if( existingValues == null ) {
				existingValues = [:]
				properties << [(propertyName):(existingValues)]
			}
			existingValues << [(environmentName):(value)]
		}
	}
	
	def getEnvironmentNames() {
		environmentNames
	}
	
	def getPropertyNames() {
		properties.keySet()
	}
	
	def getValuesForProperty( String propertyName ) {
		properties[propertyName]
	}
	
	def getEnvironment( String environmentName ) {
		if( ! environmentNames.contains( environmentName )) {
			throw new IllegalArgumentException( "Environment ${environmentName} does not exist in this model instance" )
		}
		
		def environment = [:]
		properties.each { propertyName, values ->
			def valueForEnv = values[environmentName]
			// TODO need to distinguish between key that isn't present and key with a null value?
			if( valueForEnv != null ) {
				environment << [(propertyName):(valueForEnv)]
			}
		}
		
		environment
	}
}