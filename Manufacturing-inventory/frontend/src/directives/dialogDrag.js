/**
 * el-dialog 拖拽指令（兼容旧版本 Element Plus）
 * Element Plus 2.3+ 推荐直接使用 <el-dialog draggable>
 */
export default {
  mounted(el) {
    setTimeout(() => {
      const dialogEl = el.querySelector('.el-dialog')
      const headerEl = el.querySelector('.el-dialog__header')
      if (!dialogEl || !headerEl) return

      headerEl.style.cursor = 'move'
      headerEl.style.userSelect = 'none'

      const getComputedStyle = window.getComputedStyle.bind(window)

      headerEl.onmousedown = (e) => {
        if (e.target.closest('.el-dialog__close')) return

        const startX = e.clientX
        const startY = e.clientY
        const startLeft = parseFloat(getComputedStyle(dialogEl).left) || dialogEl.offsetLeft
        const startTop = parseFloat(getComputedStyle(dialogEl).top) || dialogEl.offsetTop

        const onMouseMove = (e) => {
          const dx = e.clientX - startX
          const dy = e.clientY - startY
          dialogEl.style.left = (startLeft + dx) + 'px'
          dialogEl.style.top = (startTop + dy) + 'px'
        }

        const onMouseUp = () => {
          document.removeEventListener('mousemove', onMouseMove)
          document.removeEventListener('mouseup', onMouseUp)
        }

        document.addEventListener('mousemove', onMouseMove)
        document.addEventListener('mouseup', onMouseUp)
      }
    }, 100)
  }
}
