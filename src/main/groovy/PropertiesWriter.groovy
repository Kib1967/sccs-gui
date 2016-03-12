import java.util.Set

class PropertiesWriter {

	def writeAll( Map allProperties, String fileName ) {
	
		// One pass to get all the environment names
		def envs = [] as Set
		allProperties.each { property, envValuesMap ->
			envValuesMap.each { env, value ->
				envs << env
			}
		}

		// And make maps keyed on each
		def maps = [:]
		envs.each { env ->
			maps[env] = ['spring.profiles':(env)]
		}
		
		// Second pass to populate the per-env maps with property->value
		allProperties.each { property, envValuesMap ->
			envValuesMap.each { env, value ->
				maps[env] << [(property):(value)]
			}
		}

		def writer = new YamlWriter()
		def text = writer.write( maps.values() )
		
		def path = Paths.get( fileName )
		path.setText( text )
	}
}