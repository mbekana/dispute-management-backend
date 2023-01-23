package com.eb.disputemanagement.dispute.comment;

import com.eb.disputemanagement.dispute.EmbeddedClasses.Employee;
import com.eb.disputemanagement.dispute.config.EmployeeClient;
import com.eb.disputemanagement.dispute.disputeRequest.DisputeRequestService;
import com.eb.disputemanagement.dispute.utils.Util;
import com.eb.disputemanagement.dispute.dto.EmployeeMapper;
import com.eb.disputemanagement.dispute.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final EmployeeMapper employeeMapper;
    private  final DisputeRequestService disputeRequestService;

    private final EmployeeClient employeeClient;

    Logger logger = LogManager.getLogger(CommentServiceImpl.class);

    @Override
    public Comment createComment(long id, JwtAuthenticationToken token, Comment comment) {
        var dr = disputeRequestService.getDisputeRequest(id);
        var employeedID = Util.getEmployeeID(token);
        var employee = getEmployee(employeedID);
        comment.setStatus(CommentStatus.sent);
        comment.setSenderId(employee);
        comment.setDisputeRequest(dr);
        commentRepository.save(comment);
        return comment;
    }

    private Employee getEmployee(String employeeId) {
        return employeeMapper.employeeDtoToEmployee(employeeClient.getEmployeeById(employeeId));
    }

    @Override
    public Page<Comment> getComments(long id, Pageable pageable) {
        var dr = disputeRequestService.getDisputeRequest(id);
        return commentRepository.findAllByDisputeRequest(dr, pageable);
    }

    @Override
    public Comment updateComment(long id, JwtAuthenticationToken token, Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getComment(long id) {
        return commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(Comment.class, "id", String.valueOf(id)));
    }

    @Override
    public void deleteComment(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void readComment(long id, JwtAuthenticationToken token) {
        var comments = commentRepository.findAllByStatus(CommentStatus.sent);
//        var unSeen = dr.getComments();
        var currentEmployee = getEmployee(Util.getEmployeeID(token));
        for(Comment cm:comments){
            if(!cm.getStatus().equals(CommentStatus.seen) && !cm.getSenderId().getEmployeeId().equals(Util.getEmployeeID(token))){
                cm.setStatus(CommentStatus.seen);
                cm.setReadBy(currentEmployee);
            }
            commentRepository.save(cm);
        }
    }
}
