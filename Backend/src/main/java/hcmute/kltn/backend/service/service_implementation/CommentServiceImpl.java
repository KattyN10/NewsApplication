package hcmute.kltn.backend.service.service_implementation;

import hcmute.kltn.backend.dto.CommentDTO;
import hcmute.kltn.backend.entity.Article;
import hcmute.kltn.backend.entity.Comment;
import hcmute.kltn.backend.entity.User;
import hcmute.kltn.backend.repository.ArticleRepo;
import hcmute.kltn.backend.repository.CommentRepo;
import hcmute.kltn.backend.repository.UserRepo;
import hcmute.kltn.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ModelMapper modelMapper;
    private final CommentRepo commentRepo;
    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        Article article = articleRepo.findById(commentDTO.getArticle().getId())
                .orElseThrow(() -> new NullPointerException("No article with id: " + commentDTO.getArticle().getId()));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setUser(user);
        comment.setComment(commentDTO.getComment());
        comment.setCreate_date(LocalDateTime.now());
        if (commentDTO.getParent() != null) {
            Comment parentComment = commentRepo.findById(commentDTO.getParent().getId())
                    .orElseThrow(() -> new NullPointerException("No parent comment with id: " + commentDTO.getParent().getId()));
            comment.setParent(parentComment);
        }
        commentRepo.save(comment);
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    public String deleteComment(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NullPointerException("No comment with id: " + commentId));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        if (user != comment.getUser()) {
            throw new RuntimeException("Comments from other users cannot be deleted");
        } else {
            commentRepo.delete(comment);
            return "Deleted successfully";
        }
    }

    @Override
    public CommentDTO updateComment(CommentDTO commentDTO, String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NullPointerException("No comment with id: " + commentId));
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepo.findByEmail(name).orElseThrow();

        if (user != comment.getUser()) {
            throw new RuntimeException("Comments from other users cannot be updated");
        } else {
            LocalDateTime expirationTime = comment.getCreate_date().plusMinutes(15);
            if (LocalDateTime.now().isAfter(expirationTime)) {
                throw new RuntimeException("Comments cannot be updated after 15 minutes");
            } else {
                comment.setComment(commentDTO.getComment());
                commentRepo.save(comment);
                return modelMapper.map(comment, CommentDTO.class);
            }
        }
    }

    @Override
    public List<CommentDTO> getParentComments(String articleId) {
        Article article = articleRepo.findById(articleId)
                .orElseThrow(() -> new NullPointerException("No article with id: " + articleId));
        List<Comment> listComments = commentRepo.findParentByArticleIdOrderNewest(article.getId());
        return listComments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getChildComments(String commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NullPointerException("No comment with id: " + commentId));
        List<Comment> listComments = commentRepo.findChildByIdOrderNewest(comment.getId());
        return listComments.stream()
                .map(cmt -> modelMapper.map(cmt, CommentDTO.class))
                .collect(Collectors.toList());
    }
}
