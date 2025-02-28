import org.moqui.context.ExecutionContext
import org.moqui.entity.EntityFacade


    String productId = ec.context.productId
    EntityFacade ef = ec.entity

    // Check if the product exists
    def product = ef.find("Product").condition("productId", productId).one()
    if (!product) {
        ec.message.addError("Product with ID ${productId} does not exist.")
        return
    }

    // Check for related records
    boolean hasRelatedRecords = false

    // List of related entities to check
    List<String> relatedEntities = [
            "ProductContentEntity",
            "ProductPrice",
            "ProductDimension",
            "ProductAssoc",
            "ProductCategoryMember",
            "ProductFeatureAppl"
    ]

    for (String entityName : relatedEntities) {
        def relatedRecord = ef.find(entityName).condition("productId", productId).one()
        if (relatedRecord) {
            hasRelatedRecords = true
            break
        }
    }

    // If there are related records, delete them
    if (hasRelatedRecords) {
        ef.find("ProductContentEntity").condition("productId", productId).deleteAll()
        ef.find("ProductPrice").condition("productId", productId).deleteAll()
        ef.find("ProductDimension").condition("productId", productId).deleteAll()
        ef.find("ProductAssoc").condition("productId", productId).deleteAll()
        ef.find("ProductAssoc").condition("toProductId", productId).deleteAll()
        ef.find("ProductCategoryMember").condition("productId", productId).deleteAll()
        ef.find("ProductFeatureAppl").condition("productId", productId).deleteAll()
    }

    // Delete the Product record regardless of related records
    ef.find("Product").condition("productId", productId).deleteAll()



    return ["message": "Product  deleted successfully."]
