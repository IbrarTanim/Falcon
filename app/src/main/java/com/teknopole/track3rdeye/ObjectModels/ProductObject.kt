package com.teknopole.track3rdeye.ObjectModels

import com.google.gson.annotations.SerializedName

data class ProductObject(
        @SerializedName("Id") var id: Int,
        @SerializedName("ProductName") var productName: String,
        @SerializedName("ProductCode") var productCode: String,

        @SerializedName("CurrentUnitPrice") var currentUnitPrice: Int,
        @SerializedName("CurrentSalesPrice") var currentSalesPrice: Int,

        @SerializedName("UnitPrice") var unitPrice: Int,
        @SerializedName("SalesPrice") var salesPrice: Int,

        @SerializedName("BookedQty") var bookedQty: Int,

        @SerializedName("StockQty") var stockQty: Int,

        @SerializedName("CurrentStockQty") var currentStockQty: Int,

        @SerializedName("OrderPlaced") var orderPlaced: Boolean,

        @SerializedName("CreatedOn") var createdOn: String,
        @SerializedName("ModifiedOn") var modifiedOn: Any,
        @SerializedName("Model") var model: String,

        @SerializedName("ProductDetails") var productDetails: List<ProductDetail>,
        @SerializedName("ProductImages") var productImages: List<ProductImage>
) {
    override fun toString(): String { //this is important for fitlering in autocomplete textview
        return productName
    }
}

//data class Product(


//    @SerializedName("CurrentStockQty") var currentStockQty: Int,
//
//}

