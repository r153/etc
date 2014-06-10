;;(defun insertdate ()
;;      "this is my func" 
;;      (interactive)
;;       (let ((str (current-time-string)))
;;      (insert "//"   )(insert (substring str 20 24))
;;      (insert "/"   ) (insert (substring str 4 7))
;;      (insert "/"   )(insert (substring str 8 10))
;;      (insert " "   )(insert (substring str 11 19))))

(defun insertdate ()
      "insert date" 
      (interactive)
      (insert "//"  )
      (insert (format-time-string "%Y/%m/%d %k:%M" (current-time)))
      (insert " iida\n" ))
