(server-start)

;path
(add-to-list 'load-path "~/.emacs.d/site-lisp")
(let ((default-directory (expand-file-name "~/.emacs.d/site-lisp")))
  (normal-top-level-add-subdirs-to-load-path))

;;;;; キーバインド
;F10はdefでメニューバー選択
(define-key global-map "\C-z" 'undo)                 ; undo
(define-key global-map "\C-c\C-c" 'dabbrev-expand)   ; 補完
(global-set-key [f11] 'speedbar)					 ;一覧作成
(global-set-key [tab] 'indent-relative)				 ;tab
(global-set-key [f3] 'switch-to-buffer)				 ;buffer作成

;pageup pagdownでwindow移動
(global-set-key [next] 'other-window)
(defun back-window ()
  (interactive)
  (other-window -1))
(global-set-key [prior] 'back-window)

;macro系
(global-set-key [f1] 'start-kbd-macro)				 ;マクロ記録開始
(global-set-key [f2] 'end-kbd-macro)				 ;マクロ記録開始
(global-set-key [S-f1] 'call-last-kbd-macro)		 ;マクロ実行

;検索系
(define-key global-map "\C-f" ' isearch-forward-regexp) ; 正規表現検
;;C-sで次の該当文字列にjump		  
;;C-rで前の該当文字列にjump			   
(global-set-key "\C-c\C-g" 'goto-line) ;行jump

;;backspaceをisearch中のmini-bufで有効
(define-key isearch-mode-map [backspace] 'isearch-delete-char)

;; grep-findでackを使う

; M-x grep-by-ack
; Perlのackコマンドを使ったgrep(カーソル付近の単語をデフォルトの検索語に)
(defun grep-by-ack ()
  "grep the whole directory for something defaults to term at cursor position"
  (interactive)
  (setq default-word (thing-at-point 'symbol))
  (setq needle1 (or (read-string (concat "ack-grep for <" default-word ">: ")) default-word))
  (setq needle1 (if (equal needle1 "") default-word needle1))
  (setq default-dir default-directory)
  (setq needle2 (or (read-string (concat "target directory <" default-dir ">: ")) default-dir))
  (setq needle2 (if (equal needle2 "") default-dir needle2))
  (grep-find (concat "ack-grep --nocolor --nogroup " needle1 " " needle2)))
 
(define-key global-map "\M-s\M-g" 'grep-by-ack ) ;grep


;TAB幅の変更
(setq-default tab-width 4)

;shift+矢印で選択 
(setq pc-select-selection-keys-only t)
(pc-selection-mode 1)

;サイズ
(setq default-frame-alist (append (list '(width .  110)
                                        '(height . 40))
                                  default-frame-alist))

;色
(require 'color-theme)
(color-theme-initialize)
(color-theme-arjen)

;no backupfile
(setq backup-inhibited t)
(setq make-backup-files nil)
(setq auto-save-default nil)

;補完
(setq completion-ignore-case t)	;補完時に大文字小文字を区別しない
(partial-completion-mode 1)		;強力な補完機能 ex) p-b->print-buffer
(icomplete-mode 1)				; 補完可能なものを随時表示

;前回の編集場所を記憶する
(load "saveplace")
(setq-default save-place t)

(column-number-mode t)			;カーソルの位置が何文字目かを表示する
(show-paren-mode 1)				;対応する括弧を光らせる。
(setq scroll-step 1)			;スクロールを一行ずつにする
(set-scroll-bar-mode 'right)		;スクロールバー
(tool-bar-mode nil)				 ;ツールバーを消す
(setq inhibit-startup-message t) ;起動時のメッセージを表示しない

;speedbar
(add-hook 'speedbar-mode-hook
          '(lambda ()
             (speedbar-add-supported-extension '("")))) ;全てのファイルを表示

;shell,eshellの文字化け対策
(add-hook 'set-language-environment-hook 
	  (lambda ()
	    (when (equal "ja_JP.UTF-8" (getenv "LANG"))
	      (setq default-process-coding-system '(utf-8 . utf-8))
	      (setq default-file-name-coding-system 'utf-8))
	    (when (equal "Japanese" current-language-environment)
	      (setq default-buffer-file-coding-system 'iso-2022-jp))))

(set-language-environment "Japanese")

;ファイル指定&open
;From: http://www.emacswiki.org/cgi-bin/wiki.pl/EshellEnhancedLS
(eval-after-load "em-ls"
  '(progn
     ;; (defun ted-eshell-ls-find-file-at-point (point)
     ;;          "RET on Eshell's `ls' output to open files."
     ;;          (interactive "d")
     ;;          (find-file (buffer-substring-no-properties
     ;;                      (previous-single-property-change point 'help-echo)
     ;;                      (next-single-property-change point 'help-echo))))
     (defun pat-eshell-ls-find-file-at-mouse-click (event)
       "Middle click on Eshell's `ls' output to open files.
 From Patrick Anderson via the wiki."
       (interactive "e")
       (ted-eshell-ls-find-file-at-point (posn-point (event-end event))))
     (defun ted-eshell-ls-find-file ()
       (interactive)
       (let ((fname (buffer-substring-no-properties
                     (previous-single-property-change (point) 'help-echo)
                     (next-single-property-change (point) 'help-echo))))
         ;; Remove any leading whitespace, including newline that might
         ;; be fetched by buffer-substring-no-properties
         (setq fname (replace-regexp-in-string "^[ \t\n]*" "" fname))
         ;; Same for trailing whitespace and newline
         (setq fname (replace-regexp-in-string "[ \t\n]*$" "" fname))
         (cond
          ((equal "" fname)
           (message "No file name found at point"))
          (fname
           (find-file fname)))))
     (let ((map (make-sparse-keymap)))
       ;;          (define-key map (kbd "RET")      'ted-eshell-ls-find-file-at-point)
       ;;          (define-key map (kbd "<return>") 'ted-eshell-ls-find-file-at-point)
       (define-key map (kbd "RET")      'ted-eshell-ls-find-file)
       (define-key map (kbd "<return>") 'ted-eshell-ls-find-file)
       (define-key map (kbd "<mouse-2>") 'pat-eshell-ls-find-file-at-mouse-click)
       (defvar ted-eshell-ls-keymap map))
     (defadvice eshell-ls-decorated-name (after ted-electrify-ls activate)
       "Eshell's `ls' now lets you click or RET on file names to open them."
       (add-text-properties 0 (length ad-return-value)
                            (list 'help-echo "RET, mouse-2: visit this file"
                                  'mouse-face 'highlight
                                  'keymap ted-eshell-ls-keymap)
                            ad-return-value)
       ad-return-value)))


(custom-set-variables
  ;; custom-set-variables was added by Custom.
  ;; If you edit it by hand, you could mess it up, so be careful.
  ;; Your init file should contain only one such instance.
  ;; If there is more than one, they won't work right.
 '(blink-cursor-mode nil)
 '(column-number-mode t)
 '(cua-mode t nil (cua-base))
 ;'(current-language-environment "Japanese")
 '(display-time-mode t)
 '(show-paren-mode t))
(custom-set-faces
  ;; custom-set-faces was added by Custom.
  ;; If you edit it by hand, you could mess it up, so be careful.
  ;; Your init file should contain only one such instance.
  ;; If there is more than one, they won't work right.
 )
