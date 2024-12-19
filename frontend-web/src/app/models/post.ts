import { Comment } from "./comment";

export interface Post {
  id?: number;
  title: string;
  content: string;
  author: string;
  status?: string;
  createdAt?: string;
  // createdAt: Date;
  comments?: Comment[];
}
