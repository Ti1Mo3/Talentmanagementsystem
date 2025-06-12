import { defineStore } from 'pinia';

export const useKnowledgeStore = defineStore('knowledge', {
  state: () => ({
    items: [] as any[],
    loading: false,
    error: null as string | null,
  }),
  actions: {
    setItems(items: any[]) {
      this.items = items;
    },
    setLoading(loading: boolean) {
      this.loading = loading;
    },
    setError(error: string | null) {
      this.error = error;
    },
  },
});
