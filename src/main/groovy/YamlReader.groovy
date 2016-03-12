import java.io.File
import org.yaml.snakeyaml.*

def class YamlReader {

	def read( String text ) {
		def yaml = new Yaml()

		def yamlDocuments = yaml.loadAll( text )

		def documents = [:]

		yamlDocuments.each { yamlDocument ->
			documents[yamlDocument.spring.profiles] = flatten( yamlDocument )
		}

		documents
	}

	def flatten( Map map ) {
		def flattenedMap = [:]
		
		doFlatten(map, flattenedMap, '')
		
		flattenedMap
	}

	def doFlatten(Map map, Map flattenedMap, String prefix) {
		map.each { key, value ->
			def newKey = (prefix.isEmpty()) ? key : "${prefix}.${key}"
			if( value instanceof Map ) {
				doFlatten( value, flattenedMap, newKey )
			}
			else {
				flattenedMap[newKey] = value
			}
		}
		
		flattenedMap
	}
}