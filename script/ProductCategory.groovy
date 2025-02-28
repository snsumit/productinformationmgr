import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFacade

String productCategoryId = ec.context.productCategoryId
EntityFacade ef = ec.entity


def productCategory = ef.find("ProductCategory").condition("productCategoryId", productCategoryId).one()
if (!productCategory) {
        ec.message.addError("ProductCategory with ID ${productCategoryId} does not exist.")
        return [message: "ProductCategory not found."]
}


ef.find("ProductCategoryMember").condition("productCategoryId", productCategoryId).list().each { record ->
        record.delete()
}


ef.find("ProductCategory").condition("productCategoryId", productCategoryId).deleteAll()

return [message: "ProductCategory and related records deleted successfully."]

