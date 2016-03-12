import java.nio.file.*
import org.ajoberstar.grgit.Grgit

class Git {
	String remoteRepo
	Path localDir
	String branchName
	boolean cloneIfAbsent
	boolean autoPush
	
	Grgit grgit

	Git( String remoteRepo, String localDirName, String branchName, boolean cloneIfAbsent, boolean autoPush ) {
		this.remoteRepo = remoteRepo
		this.localDir = Paths.get( localDirName )
		this.branchName = branchName
		this.cloneIfAbsent = cloneIfAbsent
		this.autoPush = autoPush

		def debugWriter = new File( 'c:/tmp/groovy-debug.log' ).newPrintWriter()
		try {
			debugWriter.println( "In git constructor" )
			
			if( Files.isDirectory( localDir )) {
				debugWriter.println( "Initialising from local directory" )
				grgit = Grgit.init( dir: localDir.toString())
			} else {
				debugWriter.println( "Clone from remote repo" )
				grgit = Grgit.clone( dir: localDir.toString(), uri: remoteRepo )
			}
		} catch( Exception e ) {
			e.printStackTrace( debugWriter )
			throw e
		} finally {
			debugWriter.close()
		}
	}

	def checkout( String file ) {
	
		grgit.checkout( branch: branchName )
		grgit.pull()
	}
	
	def commit() {
	
		grgit.commit( message: 'Commit by SCCS editor' )
		if( autoPush ) {
			grgit.push()
		}
	}
}