// src/models/KnowledgeDomain.ts
import type { KnowledgeArea } from './KnowledgeArea';

export interface KnowledgeDomain {
  id: number;
  name: string;
  einarbeitung: boolean;
  wissensgebiet: KnowledgeArea;
}
