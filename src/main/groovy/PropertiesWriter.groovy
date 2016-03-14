import java.util.Set

class PropertiesWriter {

	def writeAll( PropertiesModel model, String fileName ) {
	
		// Need to make a map-of-maps for the YamlWriter
		model.environmentNames.each { environmentName ->
		
			def environment = model.environment[environmentName]
			//environment << ['spring.profiles':(env)]
			maps << [(environmentName):(environment)]
		}

		def writer = new YamlWriter()
		def text = writer.write( maps.values() )
		
		def path = Paths.get( fileName )
		path.setText( text )
	}
}