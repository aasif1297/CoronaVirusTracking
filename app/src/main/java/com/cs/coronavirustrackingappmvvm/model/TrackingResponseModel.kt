package com.cs.coronavirustrackingappmvvm.model

data class TrackingResponseModel(
    val exceededTransferLimit: Boolean,
    val features: List<Feature>,
    val fields: List<Field>,
    val geometryType: String,
    val globalIdFieldName: String,
    val objectIdFieldName: String,
    val spatialReference: SpatialReference,
    val uniqueIdField: UniqueIdField
)