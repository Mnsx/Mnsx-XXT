package top.mnsx.course.service;

import top.mnsx.course.dto.BindCatalogueMediaDto;
import top.mnsx.course.po.CatalogueMedia;

/**
 * @Author Mnsx_x xx1527030652@gmail.com
 */
public interface AssociationMediaService {
    /**
     * 绑定媒体
     * @param bindCatalogueMediaDto 目录媒体绑定dto
     * @return CatalogueMedia
     */
    public CatalogueMedia associationMedia(BindCatalogueMediaDto bindCatalogueMediaDto);
}
