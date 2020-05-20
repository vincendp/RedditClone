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
            UUID post_id = extractUUIDFromBytes((byte[]) object[0]);
            String title = (String) object[1];
            String link = object[2] != null ? (String) object[2] : null;
            String image_path = object[3] != null ? (String) object[3] : null;
            Date created_at = (Date) object[4];
            Integer post_type_id = (Integer) object[5];
            UUID user_id = extractUUIDFromBytes((byte[]) object[6]);
            UUID subreddit_id = extractUUIDFromBytes((byte[]) object[7]);
            String username = (String) object[8];
            String subreddit = (String) object[9];
            Integer votes = object[10] instanceof BigDecimal ? ((BigDecimal) object[10]).intValueExact() :
                    ((BigInteger) object[10]).intValueExact();
            Integer user_voted_for_post = object[11] instanceof BigDecimal ? ((BigDecimal) object[11]).intValueExact() :
                    ((BigInteger) object[11]).intValueExact();
            Integer comments = object[12] != null ? ((BigInteger) object[12]).intValueExact() : 0;

            GetPostPreviewDTO postPreviewDTO = new GetPostPreviewDTO(post_id, title, link, image_path, created_at,
                    post_type_id, user_id, subreddit_id, username, subreddit, votes, user_voted_for_post, comments);
            postPreviews.add(postPreviewDTO);
        }

        return postPreviews;
    }

    private UUID extractUUIDFromBytes(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
