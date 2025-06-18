import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Sunny Bot",
  description: "Smart, friendly support helper Discord bot",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Discover', link: '/discover' },
      { text: 'Start', link: '/start' },
      { text: 'Invite', link: 'https://discord.com/oauth2/authorize?client_id=1384182700285493380' }
    ],

    sidebar: [
      {
        text: 'Introduction',
        items: [
          { text: 'Discover', link: '/discover' },
          { text: 'Start', link: '/start' }
        ]
      },
      {
        text: 'Reference',
        items: [
          { text: 'Managing Indexes', link: '/indexes' },
          { text: 'All About Answers', link: '/answers' },
          { text: 'Configuration', link: '/configuration' }
        ]
      },
      {
        text: 'Advanced',
        items: [
          { text: 'JSON Format', link: '/json' },
          { text: 'Self-Host', link: '/self-host' }
        ]
      },
      {
        text: 'Legal',
        items: [
          { text: 'Terms of Service', link: '/terms' },
          { text: 'Privacy Policy', link: '/privacy' }
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/BogTheMudWing/Sunny' }
    ],

    search: {
      provider: 'local'
    },

    footer: {
      message: 'Released under the MIT License.',
      copyright: 'Copyright © 2025 BogTheMudWing'
    },

    logo: '/images/Sunny Transparent.png',

    editLink: {
      pattern: 'https://github.com/BogTheMudWing/Sunny/edit/main/docs/:path'
    }
  },

  locales: {
    root: {
      label: 'English',
      lang: 'en'
    }
  },

  cleanUrls: true,

  head: [['link', { rel: 'icon', href: '/favicon.ico' }]],

  lastUpdated: true
})
