package edu.gvsu.cis.wordguess
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class adapterClass(val wordsGuessed: List<GuessData>):
    RecyclerView.Adapter<adapterClass.ViewHolder>() {
    private var selectedPos = RecyclerView.NO_POSITION
    var numSelected = 0
    var previousSelectedPosition = 0
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

        /**
         * Initialize the widget variables for the recycler viewer.
         *  Recall scrambled text widget will be the scrambled text + Hidden word:
         *  asplpe    ******
         * */
        val scrambledText: TextView
        val timeToGuessWord: TextView
        val successOrFailIMage: ImageView
        var cardItem: CardView

        init{
            scrambledText = view.findViewById(R.id.rvWordScrambled)
            timeToGuessWord = view.findViewById(R.id.timeToGuessWord)
            successOrFailIMage = view.findViewById(R.id.imageSuccessOrFail)
            cardItem = view.findViewById(R.id.cardRowGuess)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.individual_recycler_element, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        vm.num = 5
        // What is this function doing?
        holder.itemView.isSelected = selectedPos == position
        val word = wordsGuessed.get(holder.getAdapterPosition())
        if (word.displayActualWord){
            holder.scrambledText.text = "${word.scrambledWord}    ${word.actualWord}"
            holder.timeToGuessWord.text = "${word.secondsToGuess} secs"
        } else {
            holder.scrambledText.text = "${word.scrambledWord}    ${word.secretWord}"
            holder.timeToGuessWord.text = "${word.secondsToGuess} secs"
        }
        if(word.successOrFailure) {
            holder.successOrFailIMage.setImageResource(R.drawable.baseline_check_circle_outline_24)
            holder.cardItem.setBackgroundColor(0x1BCE80.toInt())
        }
        else {
            holder.successOrFailIMage.setImageResource(R.drawable.baseline_block_24)
        }

        /**
         * move logic for numSelected to be in viewmode, and laos previous SelectedPosition
         * position is same position as array.
         */
//        selectedPos will be automatically be updated to be the index of the elmeent slected in our recyclerView.
        holder.itemView.setOnLongClickListener {
            /**
             * If no previous word has been selected, display word (needs to be moved
             * to ViewModel
             */
            previousSelectedPosition = selectedPos
            selectedPos = holder.getAdapterPosition()
            if (numSelected == 0) {
                numSelected++
//                previousSelectedPosition = selectedPos
                holder.scrambledText.text = "${word.scrambledWord}    ${word.actualWord}"
            } else {
                /**
                 * If current displayed word was selected, just hide it.
                 */
                if (selectedPos == previousSelectedPosition){
                    numSelected--
                    holder.scrambledText.text = "${word.scrambledWord}    ${word.secretWord}"
                }
                else {
                    wordsGuessed[previousSelectedPosition].displayActualWord = false
                    wordsGuessed[selectedPos].displayActualWord = true
                    this.notifyDataSetChanged()
//                    holder.scrambledText.text = "${word.scrambledWord}    ${word.actualWord}"
                }
            }
            true

        }

//        holder.itemView.setOnLongClickListener {
//            selectListener(word, true)
//            true
//        }
    }

    override fun getItemCount(): Int {
        return wordsGuessed.size
    }



}




//class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    val textView: TextView
//
//    init {
//        textView = view.findViewById(R.id.the_text)
//    }
//}
