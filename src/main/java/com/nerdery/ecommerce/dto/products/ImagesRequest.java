package com.nerdery.ecommerce.dto.products;

import org.springframework.web.multipart.MultipartFile;

public record ImagesRequest(
         Long productId,
         MultipartFile file
) {
}
