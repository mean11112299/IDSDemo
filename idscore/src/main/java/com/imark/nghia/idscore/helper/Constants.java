package com.imark.nghia.idscore.helper;

/**
 * Created by Nghia-PC on 9/11/2015.
 * Hằng số App sử dụng chung
 */
public class Constants {

    /** GROUP CODE CỦA ImageType, dùng đẻ lấy ListCodeDetail */
    public static final String GROUP_CODE_IMAGE_TYPE = "ImageType";

    /** Lưu chạy lần đầu tiên */
    public static final String PREF_KEY_FIRST_INIT = "first_init";

    /** Request code khi chụp ảnh **/
    public static final int REQUEST_CODE_CAPTURE_IMAGE = 100;

    /** Level của Tỉnh/ thành phố */
    public static final int AREA_LEVEL_CITY = 1;

    /** Level của quận/ huyện */
    public static final int AREA_LEVEL_DISTRICT = 2;

    /** Không trả về Session Code */
    public static final String DEFAULT_SESSION_CODE = "Server không trả về Session Code";

    /** Không trả về Id */
    public static final int DEFAULT_ID = 0;

    public static final String DEFAULT_LATITUDE = "0";
    public static final String DEFAULT_LONGITUDE = "0";

    /** Giá trị Long mặc định */
    public static final long DEFAULT_LONG = -1;
}
