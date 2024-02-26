package kr.co.shophub.shophub.geo.dto

data class KakaoAddressResult(
    val documents: List<KakaoAddressDocument>,
    val meta: KakaoAddressMeta,
)

data class KakaoAddressDocument(
    val address: KakaoAddress,
    val address_name: String,
    val address_type: String,
    val road_address: KakaoRoadAddress?,
    val x: String,
    val y: String,
)

data class KakaoAddress(
    val address_name: String,
    val b_code: String,
    val h_code: String,
    val main_address_no: String,
    val mountain_yn: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_h_name: String,
    val region_3depth_name: String,
    val sub_address_no: String,
    val x: String,
    val y: String
)
data class KakaoRoadAddress(
    val address_name: String,
    val building_name: String,
    val main_building_no: String,
    val region_1depth_name: String,
    val region_2depth_name: String,
    val region_3depth_name: String,
    val road_name: String,
    val sub_building_no: String,
    val underground_yn: String,
    val x: String,
    val y: String,
    val zone_no: String
)

data class KakaoAddressMeta(
    val is_end: Boolean,
    val pageable_count: Int,
    val total_count: Int
)
