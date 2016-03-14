class PropertiesReader {

	def readAll( String text, PropertiesModel model ) {
		def reader = new YamlReader()
		def documents = reader.read( text )

		documents.each { environmentName, document ->
			model.addEnvironment( environmentName, document )
		}
	}
}