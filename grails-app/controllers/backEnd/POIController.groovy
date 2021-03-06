package backEnd
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import entities.*

@Transactional(readOnly = true)
class POIController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond POI.list(params), model: [POIInstanceCount: POI.count()]
    }

    def show(POI POIInstance) {
        respond POIInstance
    }

    def create() {
        respond new POI(params)
    }

    @Transactional
    def save(POI POIInstance) {
        if (POIInstance == null) {
            notFound()
            return
        }

        if (POIInstance.hasErrors()) {
            respond POIInstance.errors, view: 'create'
            return
        }

        POIInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'POI.label', default: 'POI'), POIInstance.id])
                redirect POIInstance
            }
            '*' { respond POIInstance, [status: CREATED] }
        }
    }

    def edit(POI POIInstance) {
        respond POIInstance
    }

    @Transactional
    def update(POI POIInstance) {
        if (POIInstance == null) {
            notFound()
            return
        }

        if (POIInstance.hasErrors()) {
            respond POIInstance.errors, view: 'edit'
            return
        }

        POIInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'POI.label', default: 'POI'), POIInstance.id])
                redirect POIInstance
            }
            '*' { respond POIInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(POI POIInstance) {

        if (POIInstance == null) {
            notFound()
            return
        }

        POIInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'POI.label', default: 'POI'), POIInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'POI.label', default: 'POI'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
