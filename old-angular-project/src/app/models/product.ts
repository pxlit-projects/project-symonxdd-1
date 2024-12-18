import { Category } from './category';
import { Label } from './label';

export interface Product {
  id?: number;
  name: string;
  description: string;
  price: number;
  categoryName: string;
  labelNames: string[];
  stock: number;
  imageUrl: string;
}
