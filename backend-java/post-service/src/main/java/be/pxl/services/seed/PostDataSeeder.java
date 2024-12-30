package be.pxl.services.seed;

import be.pxl.services.domain.Post;
import be.pxl.services.domain.PostStatus;
import be.pxl.services.domain.UserRoles;
import be.pxl.services.repo.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(1) // Ensures this runs early
public class PostDataSeeder implements CommandLineRunner {

    private final PostRepository postRepository;

    public PostDataSeeder(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) { // Check if the database is empty
            postRepository.saveAll(
                    List.of(
                            Post.builder()
                                    .title("The Future of Technology")
                                    .content("Exploring the latest advancements in AI and robotics.")
                                    .author(UserRoles.EDITOR.getDisplayName())
                                    .status(PostStatus.APPROVED)
                                    .createdAt(LocalDateTime.now().minusDays(10))
                                    .updatedAt(LocalDateTime.now().minusDays(2))
                                    .build(),
                            Post.builder()
                                    .title("The Importance of Mental Health")
                                    .content("Why mental health should be prioritized.")
                                    .author(UserRoles.EDITOR.getDisplayName())
                                    .status(PostStatus.PENDING_REVIEW)
                                    .createdAt(LocalDateTime.now().minusDays(5))
                                    .updatedAt(LocalDateTime.now())
                                    .build(),
                            Post.builder()
                                    .title("Sustainable Living")
                                    .content("Tips and tricks for an eco-friendly lifestyle.")
                                    .author(UserRoles.EDITOR.getDisplayName())
                                    .status(PostStatus.REJECTED)
                                    .createdAt(LocalDateTime.now().minusDays(8))
                                    .updatedAt(LocalDateTime.now().minusDays(3))
                                    .build(),
                            Post.builder()
                                    .title("A Beginner's Guide to Cloud Computing")
                                    .content("Discover the fundamentals of cloud computing and how to get started with popular platforms like AWS, Azure, and Google Cloud.")
                                    .author(UserRoles.EDITOR.getDisplayName())
                                    .status(PostStatus.DRAFT)
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .build()
                    )
            );
        }
    }
}
