package com.vincendp.RedditClone.Utility;

import com.vincendp.RedditClone.Dto.GetPostPreviewDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class ProjectionUtility {

    public List<GetPostPreviewDTO> getPostPreviewDTO(List<Object[]> objectList){
        List<GetPostPreviewDTO> postPreviews = new ArrayList<>();
        for(Object[] object: objectList){
            GetPostPreviewDTO postPreviewDTO = new GetPostPreviewDTO(
                extractUUIDFromBytes((byte[]) object[0]),
                (String) object[1],
                object[2] != null ? (String) object[2] : null,
                object[3] != null ? (String) object[3] : null,
                (Date) object[4],
                (Integer) object[5],
                extractUUIDFromBytes((byte[]) object[6]),
                extractUUIDFromBytes((byte[]) object[7]),
                (String) object[8],
                (String) object[9],
                ((BigDecimal) object[10]).intValueExact(),
                ((BigDecimal) object[11]).intValueExact(),
                object[12] != null ? ((BigInteger) object[12]).intValueExact() : 0
            );
            postPreviews.add(postPreviewDTO);
        }

        return postPreviews;
    }

    private UUID extractUUIDFromBytes(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
