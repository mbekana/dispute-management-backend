package com.eb.disputemanagement.dispute.comment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment(CommentDto commentDto);
    CommentDto toCommentDto(Comment comment);
}
