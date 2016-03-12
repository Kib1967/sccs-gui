import java.io.File
import org.yaml.snakeyaml.*

def class YamlWriter {

	def write( Collection maps ) {

		def documents = []

		maps.each { map ->
			documents << expand(map)
		}
		
		def yaml = new Yaml()
		
		yaml.dumpAll( documents.iterator())
	}

	def expand( Map map ) {
		def expandedMap = [:]
		
		map.each { key, value ->
			doExpandSingle( expandedMap, key, value )
		}
		
		expandedMap
	}

	def doExpandSingle( Map expandedMap, String key, Object value ) {
		if( key.contains( '.' )) {
			def ( prefix, remainingKey ) = key.split( '\\.', 2 )
			def subMap = [:]
			expandedMap << [(prefix):(subMap)]
			doExpandSingle( subMap, remainingKey, value )
		} else {
			expandedMap << [(key):(value)]
		}
	}
}