<script setup>
import { ref } from 'vue'
defineProps(['authorUrl', 'authorName', 'messageContent', 'embedTitle', 'embedDescription'])
</script>

<style>
.discordpfp {
    cursor: pointer;
    border-radius: 100%;
    width: 50px;
    height: 50px;
}

.discordcontainer {
    display: flex;
    gap: 20px;
}

.discordname,
.discordmessage {
    margin: 0;
}

.discordname {
    cursor: pointer;
    font-weight: bold;
}

.embed {
    display: flex;
    gap: 8px;
    flex-direction: column;
    border-left-color: hsla(64, calc(var(--saturation-factor, 1) * 98.1%), 59.2%, 1);
    background: #88888811;
    border: 1px solid #88888811;
    border-radius: 4px;
    border-left: 4px solid rgb(143, 133, 0);
    padding: 8px;
    line-height: 1.5;
    max-width: 500px;
}
.embed :first-child {
    font-size: medium;
    font-weight: bold;
    color: var(--vp-c-brand-1);
    text-decoration: underline;
    cursor: pointer;
}
.embed span {
    font-size: small;
}

.actionrow {
    display: flex;
    flex-wrap: wrap;
    padding: 8px 0;
    gap: 8px;
}
.button {
    font-size: small;
    font-weight: bold;
    border: 1px solid #88888811;
    border-radius: 8px;
    padding: 4px 18px;
    cursor: pointer;
}

.successType {
    background-color: #00863A;
    color: white;
}

.secondaryType, .linkType {
    background-color: #322E2B;
    color: white;
}
</style>

<template>
    <div class="discordcontainer">
        <img class="discordpfp" :src=authorUrl alt="User"></img>
        <div>
            <span class="discordname">{{ authorName }}</span>
            <br>
            <span class="discordmessage">{{ messageContent }}</span>
            <template v-if="embedTitle">
                <div class="embed">
                    <span>{{ embedTitle }}</span>
                    <span>{{ embedDescription }}</span>
                </div>
                <div class="actionrow">
                    <span class="button successType">Mark answer</span>
                    <span class="button secondaryType">Not helpful</span>
                    <span class="button linkType">Open resource ↗</span>
                </div>
            </template>
        </div>
    </div>
</template>