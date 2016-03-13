class PropertiesReader {

	def readAll( String text, PropertiesModel model ) {
		def reader = new YamlReader()
		def documents = reader.read( text )

		documents.each { document ->
			def environmentName = document[spring.profiles]
			model.addEnvironment( environmentName, document )
		}
	}
}