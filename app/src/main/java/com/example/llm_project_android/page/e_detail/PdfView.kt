package com.example.llm_project_android.page.e_detail

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.llm_project_android.R
import com.example.llm_project_android.adapter.PdfPageAdapter
import com.example.llm_project_android.functions.getPassedExtras
import java.io.File

class PdfView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var closeButton: ImageButton
    private lateinit var pageIndicator: TextView

    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null

    var name: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.e_page_pdf_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras = getPassedExtras("pdf", String::class.java)
        name = extras["pdf"] as? String ?: ""  // pdf 값 intent에서 수신

        recyclerView = findViewById(R.id.pdfRecyclerView)
        closeButton = findViewById(R.id.closeButton)
        pageIndicator = findViewById(R.id.pageIndicator)

        openPdf(name+".pdf")
        click_CloseButton()
        scroll_Pdf()
    }

    // pdf 파일 열기
    private fun openPdf(fileName: String) {
        val file = File(cacheDir, fileName)
        if (!file.exists()) {
            assets.open(fileName).use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(fileDescriptor!!)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PdfPageAdapter(pdfRenderer!!)
        pageIndicator.text = "1 / ${pdfRenderer!!.pageCount}"
    }

    override fun onDestroy() {
        pdfRenderer?.close()
        fileDescriptor?.close()
        super.onDestroy()
    }
    
    // 닫기 버튼 클릭 이벤트
    private fun click_CloseButton() {
        closeButton.setOnClickListener {
            finish()
        }
    }
    
    // pdf 스크롤 이벤트
    private fun scroll_Pdf() {
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.recycler_divider)!!)
        recyclerView.addItemDecoration(divider)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val firstVisible = layoutManager.findFirstVisibleItemPosition() + 1
                pageIndicator.text = "$firstVisible / ${pdfRenderer?.pageCount ?: 0}"
            }
        })
    }
}