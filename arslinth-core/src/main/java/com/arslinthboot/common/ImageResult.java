package com.arslinthboot.common;

import lombok.Data;

/**
 * @author Arslinth
 * @ClassName ImageResult
 * @Description 图片滑动图片
 * @Date 2022/2/10
 */
@Data
public class ImageResult {
    int xpos;
    int ypos;
    int cutImageWidth;
    int cutImageHeight;

    String cutImage;
    String oriImage;

}
