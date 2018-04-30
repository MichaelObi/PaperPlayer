package xyz.michaelobi.paperplayer.mvp


/**
 * PaperPlayer
 * Michael Obi
 * 30/04/2018, 9:25 PM
 */
interface DataView : Mvp.View {
    fun showLoading()

    fun hideLoading()

    fun showError(message: String)
}