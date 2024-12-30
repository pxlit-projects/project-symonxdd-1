import { UserRoles } from "../enums/user-roles.enum";

export interface Comment {
  id?: number;
  postId?: number;  // Add this line to associate the comment with the post
  content: string;
  createdAt?: string;
  author?: UserRoles;
}
