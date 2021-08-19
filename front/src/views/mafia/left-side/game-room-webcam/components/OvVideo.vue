<template>
  <video
    ref="myWebCam"
    @mouseover="showVideoMenu"
    :class="{'webcam': true, 'selected-border': isSelected, 'died-user': isDead}"
    autoplay
    playsinline
    controls="false"/>
  <canvas width="620" height="320" class="canvas" ref="myCanvas"></canvas>
  <button @click="startExpressDetection" class="button-z">인식 시작</button>
</template>

<script>
import { onMounted, computed, reactive, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useStore } from 'vuex'
import * as faceapi from 'face-api.js'

export default {
  name: 'OvVideo',
  props: {
    streamManager: Object,
    isSelected: Boolean,
    isDead: Boolean,
    username: String,
  },
  setup(props, {emit}) {
    const store = useStore()
    const myWebCam = ref(null)
    const myCanvas = ref(null)
    let ctx

    const state = reactive({
      detections: null,
      myEmotion: '',
      webCamWidth: 0,
      webCamHeight: 0,
      hide: false,
      lie: false,
      timerId: 0,
      mafiaManager: computed(() => store.getters['root/mafiaManager']),
    })

    const startExpressDetection = async () => {
      store.state.root.mafiaManager.lierItem = false
      store.state.root.mafiaManager.isLierItemActivate = false

      const displaySize = { width: myWebCam.value.videoWidth, height: myWebCam.value.videoHeight }
      faceapi.matchDimensions(myCanvas.value, displaySize)

      state.timerId = setInterval(async () => {
        ctx.clearRect(0, 0, 620, 320)
        state.detections = await faceapi.detectSingleFace(myWebCam.value, new faceapi.TinyFaceDetectorOptions())
          .withFaceLandmarks()
          .withFaceExpressions()
        console.log(state.detections)
        const resizedDetections = faceapi.resizeResults(state.detections, displaySize)
        faceapi.draw.drawFaceLandmarks(myCanvas.value, resizedDetections)

      }, 700)

      setTimeout(() => {
        clearInterval(state.timerId)
      }, 500000)
    }

    onMounted(async () => {
      ctx = myCanvas.value.getContext('2d')
      props.streamManager.addVideoElement(myWebCam.value)
      await faceapi.nets.tinyFaceDetector.load('/static/models')
      await faceapi.nets.faceExpressionNet.load('/static/models')
      await faceapi.nets.faceLandmark68Net.load('/static/models')
    })

    return { state, myWebCam, myCanvas, startExpressDetection }
  }
}
</script>
<style scoped>
  @import url('OvVideo.css');
</style>

