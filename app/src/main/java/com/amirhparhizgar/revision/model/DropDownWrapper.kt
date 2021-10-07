package com.amirhparhizgar.revision.model

data class DropDownWrapper<T>(
    var list: List<T>,
    var selected: Int
)