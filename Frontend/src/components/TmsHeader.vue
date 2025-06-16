<template>
  <header class="tms-header">
    <div class="tms-header-left">
      <span class="tms-logo">TMS</span>
      <nav class="tms-nav">
        <a
          v-for="item in menuItems"
          :key="item.key"
          href="#"
          class="tms-nav-link"
          :class="{ selected: selectedMenu === item.key }"
          @click.prevent="$emit('menu-select', item.key)"
        >
          {{ item.label }}
        </a>
      </nav>
    </div>
    <div class="tms-header-right">
      <span class="tms-datenmanager">Datenmanager</span>
      <span class="tms-user-icon"><svg width="22" height="22" viewBox="0 0 22 22"><circle cx="11" cy="7" r="4" fill="#222"/><ellipse cx="11" cy="16" rx="7" ry="4" fill="#222"/></svg></span>
    </div>
  </header>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from 'vue';

const props = defineProps<{ selectedMenu: string }>();
const emit = defineEmits(['menu-select']);

const menuItems = [
  { key: 'areas', label: 'Wissensgebiete verwalten' },
  { key: 'domains', label: 'Wissensbereiche verwalten' },
  { key: 'modules', label: 'Wissensbausteine verwalten' },
  { key: 'paths', label: 'Ausbildungspfade verwalten' },
];
</script>

<style scoped>
.tms-header {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
  width: 100vw;
  background: linear-gradient(90deg, #1d4ed8 0%, #2563eb 100%);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 2vw;
  box-sizing: border-box;
  min-height: 62px;
  font-family: inherit;
  box-shadow: 0 2px 12px 0 rgba(29,78,216,0.10);
  border-radius: 0 0 18px 18px;
}
.tms-header-left {
  display: flex;
  align-items: center;
  gap: 2vw;
}
.tms-logo {
  font-size: 2.5rem;
  font-weight: 900;
  letter-spacing: 0.04em;
  color: #fff;
  margin-right: 1.5vw;
  text-shadow: 0 2px 8px rgba(29,78,216,0.18);
}
.tms-nav {
  display: flex;
  gap: 1.2vw;
}
.tms-nav-link {
  font-size: 1.18rem;
  color: #fff;
  text-decoration: none;
  font-weight: 600;
  padding: 0.3rem 1.1rem;
  border-radius: 7px;
  background: transparent;
  transition: background 0.18s, color 0.18s, font-weight 0.18s;
  border: none;
}
.tms-nav-link.selected {
  font-weight: 800;
  background: #fff;
  color: #1d4ed8;
  box-shadow: 0 2px 8px 0 rgba(29,78,216,0.10);
}
.tms-nav-link:hover {
  background: #2563eb;
  color: #fff;
}
.tms-header-right {
  display: flex;
  align-items: center;
  gap: 1vw;
  font-size: 1.18rem;
  font-weight: 700;
  color: #fff;
}
.tms-datenmanager {
  margin-right: 0.5vw;
  background: #2563eb;
  color: #fff;
  padding: 0.22rem 0.9rem;
  border-radius: 5px;
  font-weight: 700;
  letter-spacing: 0.01em;
  font-size: 1.13rem;
}
.tms-user-icon svg circle,
.tms-user-icon svg ellipse {
  fill: #fff;
  stroke: #1d4ed8;
  stroke-width: 1.2;
}
.tms-user-icon {
  display: flex;
  align-items: center;
  background: #2563eb;
  border-radius: 50%;
  padding: 0.18rem;
}
body {
  padding-top: 62px; /* Platz für fixierten Header schaffen */
}
@media (max-width: 900px) {
  .tms-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 0.7rem 1vw;
    border-radius: 0 0 12px 12px;
  }
  body {
    padding-top: 80px;
  }
}
@media (max-width: 600px) {
  .tms-header {
    flex-direction: column;
    align-items: flex-start;
    padding: 0.5rem 0.5vw;
    border-radius: 0 0 8px 8px;
  }
  .tms-logo {
    font-size: 1.5rem;
  }
  .tms-nav-link {
    font-size: 1rem;
    padding: 0.13rem 0.5rem;
  }
  body {
    padding-top: 90px;
  }
}
</style>
