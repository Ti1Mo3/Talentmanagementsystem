// src/models/KnowledgeSection.ts
import type { KnowledgeArea } from './KnowledgeArea';

export interface KnowledgeSection {
  id: number;
  name: string;
  einarbeitung: boolean;
  wissensgebiet: KnowledgeArea;
}
