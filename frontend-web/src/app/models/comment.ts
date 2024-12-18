export interface Comment {
  postId: number;  // Add this line to associate the comment with the post
  content: string;
  createdAt: string;
  author: string;
}
