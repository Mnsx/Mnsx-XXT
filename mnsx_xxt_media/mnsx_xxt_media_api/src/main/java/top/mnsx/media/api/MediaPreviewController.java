package top.mnsx.media.api;

import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mnsx.exception.XXTException;
import top.mnsx.media.po.MediaFile;
import top.mnsx.media.service.MediaFileService;
import top.mnsx.model.RestResponse;

@RestController
@RequestMapping("/preview")
public class MediaPreviewController {

    @Autowired
    private MediaFileService mediaFileService;

    @GetMapping("/media/{mediaId}")
    public RestResponse<String> getVideoUrlByMediaId(@PathVariable("mediaId") String mediaId) {
        MediaFile mediaFile = mediaFileService.getFileById(mediaId);
        if (mediaFile == null || StringUtils.isEmpty(mediaFile.getUrl())) {
            XXTException.cast("视频没有转码");
        }
        return RestResponse.success(mediaFile.getUrl());
    }
}
