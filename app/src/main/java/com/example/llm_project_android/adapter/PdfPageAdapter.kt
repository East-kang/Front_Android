package com.example.llm_project_android.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class PdfPageAdapter(
    private val pdfRenderer: PdfRenderer,
    private val scale: Float = 1.0f
) : RecyclerView.Adapter<PdfPageAdapter.PdfPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfPageViewHolder {
        val imageView = ImageView(parent.context).apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, 20, 0, 20)
        }
        return PdfPageViewHolder(imageView)
    }

    override fun getItemCount(): Int = pdfRenderer.pageCount

    override fun onBindViewHolder(holder: PdfPageViewHolder, position: Int) {
        val page = pdfRenderer.openPage(position)
        val originalWidth = page.width
        val originalHeight = page.height

        // 가로 기준 최대 1080px로 제한
        val maxWidth = 1080f
        val scale = min(2.0f, maxWidth / originalWidth)

        val width = (originalWidth * scale).toInt()
        val height = (originalHeight * scale).toInt()

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        (holder.itemView as ImageView).setImageBitmap(bitmap)
    }

    class PdfPageViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
