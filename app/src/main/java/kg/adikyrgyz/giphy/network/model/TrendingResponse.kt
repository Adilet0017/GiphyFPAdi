package kg.adikyrgyz.giphy.network.model

data class TrendingResponse(
    val data: List<GifItem>,
    val pagination: Pagination
)

