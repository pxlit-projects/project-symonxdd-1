package be.pxl.services.seed;

import be.pxl.services.domain.Comment;
import be.pxl.services.domain.UserRoles;
import be.pxl.services.repo.CommentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(2) // Ensures this runs after PostDataSeeder
public class CommentDataSeeder implements CommandLineRunner {

    private final CommentRepository commentRepository;

    public CommentDataSeeder(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (commentRepository.count() == 0) { // Check if the database is empty
            commentRepository.saveAll(
                    List.of(
                            Comment.builder()
                                    .postId(1L)
                                    .content("This is such a well-written post!")
                                    .author(UserRoles.USER1.getDisplayName())
                                    .createdAt(LocalDateTime.now().minusDays(2))
                                    .updatedAt(LocalDateTime.now())
                                    .build(),
                            Comment.builder()
                                    .postId(1L)
                                    .content("I completely agree with your points!")
                                    .author(UserRoles.USER2.getDisplayName())
                                    .createdAt(LocalDateTime.now().minusDays(1))
                                    .updatedAt(LocalDateTime.now())
                                    .build(),
                            Comment.builder()
                                    .postId(2L)
                                    .content("Mental health awareness is crucial.")
                                    .author(UserRoles.EDITOR.getDisplayName())
                                    .createdAt(LocalDateTime.now().minusHours(10))
                                    .updatedAt(LocalDateTime.now())
                                    .build()
                    )
            );
        }
    }
}
