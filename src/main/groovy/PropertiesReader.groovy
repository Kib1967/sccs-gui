class PropertiesReader {

	def readAll( String text ) {
		def reader = new YamlReader()
		def documents = reader.read( text )

		def allProperties = [:]
		documents.each { documentsKey, document ->
			document.each { key, value ->
				if( !key.equals( 'spring.profiles' )) {
					allProperties << [(key):[:]]
				}
			}
		}

		allProperties.each { key, submap ->
			documents.each { documentsKey, document ->
				submap << [(documentsKey): document[key]]
			}
		}
		
		allProperties
	}
}