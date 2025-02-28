import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFacade



String productFeatureId = ec.context.productFeatureId
EntityFacade ef = ec.entity


def productFeature = ef.find("ProductFeature").condition("productFeatureId", productFeatureId).one()
if (!productFeature) {
        ec.message.addError("ProductFeature with ID ${productFeatureId} does not exist.")
        return [message: "ProductFeature not found."]
}

    // Delete related records first (handling composite PKs)
ef.find("ProductFeatureAppl").condition("productFeatureId", productFeatureId).list().each { record ->
        record.delete()
}

    // Delete the ProductFeature itself
ef.find("ProductFeature").condition("productFeatureId", productFeatureId).deleteAll()

return [message: "ProductFeature and related records deleted successfully."]

