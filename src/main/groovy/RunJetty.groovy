import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.*
import groovy.servlet.*

class RunJetty {

	static void main(String[] args) {
		def runJetty = new RunJetty()
		runJetty.run()
	}
	
	def run() {
		def jetty = new Server(9090)
		
		def handler = new ServletContextHandler(ServletContextHandler.SESSIONS)
		handler.contextPath = '/'
		handler.resourceBase = '.'
		handler.addServlet(GroovyServlet, '*.groovy')
		
		// Init parameters for this application, should be external
		handler.setInitParameter('git.remote', 'ssh://git@git.fostermoore.com:7999/sccp/fm-spring-cloud-config.git')
		handler.setInitParameter('git.local', 'c:/tmp/fm-spring-cloud-config.git')
		handler.setInitParameter('git.clone-if-absent', 'true')
		handler.setInitParameter('git.branch', 'master')
		handler.setInitParameter('git.auto-push', 'false')
		handler.setInitParameter('properties.file', 'gets.yml')
		
		def filesHolder = handler.addServlet(DefaultServlet, '/')
		filesHolder.setInitParameter('resourceBase', './static')
	 
		jetty.handler = handler
		jetty.start()
	}
}