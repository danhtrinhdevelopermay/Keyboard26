class iOS26Keyboard {
    constructor() {
        this.keyboard = document.getElementById('keyboard');
        this.outputText = document.getElementById('output-text');
        this.textInput = document.getElementById('text-input');
        this.isShift = false;
        this.isCapsLock = false;
        this.currentMode = 'letters';
        this.typedText = '';
        
        this.layouts = {
            letters: {
                row1: ['Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P'],
                row2: ['A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L'],
                row3: ['Z', 'X', 'C', 'V', 'B', 'N', 'M']
            },
            numbers: {
                row1: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '0'],
                row2: ['-', '/', ':', ';', '(', ')', '$', '&', '@'],
                row3: ['.', ',', '?', '!', "'"]
            },
            symbols: {
                row1: ['[', ']', '{', '}', '#', '%', '^', '*', '+', '='],
                row2: ['_', '\\', '|', '~', '<', '>', '€', '£', '¥'],
                row3: ['•', '©', '®', '™', '°']
            }
        };
        
        this.init();
    }
    
    init() {
        this.renderKeyboard();
        this.setupEventListeners();
    }
    
    renderKeyboard() {
        const layout = this.layouts[this.currentMode];
        
        document.getElementById('row-1').innerHTML = this.renderRow(layout.row1);
        document.getElementById('row-2').innerHTML = this.renderRow(layout.row2);
        document.getElementById('row-3').innerHTML = this.renderRow3(layout.row3);
        document.getElementById('row-4').innerHTML = this.renderRow4();
    }
    
    renderRow(keys) {
        return keys.map(key => {
            const displayKey = this.isShift || this.isCapsLock ? key.toUpperCase() : key.toLowerCase();
            return `<div class="key letter" data-key="${key}" data-char="${displayKey}">${displayKey}</div>`;
        }).join('');
    }
    
    renderRow3(keys) {
        let html = `
            <div class="key special shift" data-action="shift">
                <svg viewBox="0 0 24 24"><path d="M12 4L4 12h4v8h8v-8h4L12 4z"/></svg>
            </div>
        `;
        
        html += keys.map(key => {
            const displayKey = this.isShift || this.isCapsLock ? key.toUpperCase() : key.toLowerCase();
            return `<div class="key letter" data-key="${key}" data-char="${displayKey}">${displayKey}</div>`;
        }).join('');
        
        html += `
            <div class="key special backspace" data-action="backspace">
                <svg viewBox="0 0 24 24"><path d="M22 3H7c-.69 0-1.23.35-1.59.88L0 12l5.41 8.11c.36.53.9.89 1.59.89h15c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-3 12.59L17.59 17 14 13.41 10.41 17 9 15.59 12.59 12 9 8.41 10.41 7 14 10.59 17.59 7 19 8.41 15.41 12 19 15.59z"/></svg>
            </div>
        `;
        
        return html;
    }
    
    renderRow4() {
        let modeBtn = '';
        let letterBtn = '';
        
        if (this.currentMode === 'letters') {
            modeBtn = `<div class="key special numbers" data-action="numbers">123</div>`;
        } else if (this.currentMode === 'numbers') {
            modeBtn = `<div class="key special symbols" data-action="symbols">#+=</div>`;
            letterBtn = `<div class="key special letters" data-action="letters">ABC</div>`;
        } else {
            modeBtn = `<div class="key special numbers" data-action="numbers">123</div>`;
            letterBtn = `<div class="key special letters" data-action="letters">ABC</div>`;
        }
        
        if (this.currentMode === 'letters') {
            return `
                ${modeBtn}
                <div class="key special globe" data-action="globe">
                    <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/></svg>
                </div>
                <div class="key letter space" data-key=" " data-char=" ">space</div>
                <div class="key special return" data-action="return">Search</div>
            `;
        } else {
            return `
                ${letterBtn || modeBtn}
                <div class="key special globe" data-action="globe">
                    <svg viewBox="0 0 24 24" width="18" height="18"><path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z"/></svg>
                </div>
                <div class="key letter space" data-key=" " data-char=" ">space</div>
                <div class="key special return" data-action="return">return</div>
            `;
        }
    }
    
    setupEventListeners() {
        this.keyboard.addEventListener('mousedown', (e) => this.handleKeyDown(e));
        this.keyboard.addEventListener('mouseup', (e) => this.handleKeyUp(e));
        this.keyboard.addEventListener('mouseleave', (e) => this.handleKeyUp(e));
        
        this.keyboard.addEventListener('touchstart', (e) => {
            e.preventDefault();
            this.handleKeyDown(e);
        }, { passive: false });
        
        this.keyboard.addEventListener('touchend', (e) => {
            e.preventDefault();
            this.handleKeyUp(e);
        }, { passive: false });
        
        document.getElementById('light-mode').addEventListener('click', () => {
            this.keyboard.classList.remove('dark-mode');
            document.getElementById('light-mode').classList.add('active');
            document.getElementById('dark-mode').classList.remove('active');
        });
        
        document.getElementById('dark-mode').addEventListener('click', () => {
            this.keyboard.classList.add('dark-mode');
            document.getElementById('dark-mode').classList.add('active');
            document.getElementById('light-mode').classList.remove('active');
        });
    }
    
    handleKeyDown(e) {
        const target = e.target.closest('.key');
        if (!target) return;
        
        target.classList.add('pressed');
        
        this.createRipple(target);
        
        if ('vibrate' in navigator) {
            navigator.vibrate(10);
        }
    }
    
    handleKeyUp(e) {
        const target = e.target.closest('.key');
        if (!target) return;
        
        target.classList.remove('pressed');
        
        const key = target.dataset.key;
        const action = target.dataset.action;
        
        if (key) {
            this.typeKey(key);
        } else if (action) {
            this.handleAction(action);
        }
    }
    
    typeKey(key) {
        let char = key;
        
        if (this.currentMode === 'letters') {
            char = this.isShift || this.isCapsLock ? key.toUpperCase() : key.toLowerCase();
        }
        
        this.typedText += char;
        this.updateOutput();
        
        if (this.isShift && !this.isCapsLock) {
            this.isShift = false;
            this.renderKeyboard();
        }
    }
    
    handleAction(action) {
        switch (action) {
            case 'shift':
                if (this.isShift) {
                    this.isCapsLock = true;
                    this.isShift = false;
                } else if (this.isCapsLock) {
                    this.isCapsLock = false;
                } else {
                    this.isShift = true;
                }
                this.renderKeyboard();
                break;
                
            case 'backspace':
                this.typedText = this.typedText.slice(0, -1);
                this.updateOutput();
                break;
                
            case 'return':
                this.typedText += '\n';
                this.updateOutput();
                break;
                
            case 'numbers':
                this.currentMode = 'numbers';
                this.renderKeyboard();
                break;
                
            case 'symbols':
                this.currentMode = 'symbols';
                this.renderKeyboard();
                break;
                
            case 'letters':
                this.currentMode = 'letters';
                this.renderKeyboard();
                break;
                
            case 'globe':
                this.animateGlobe();
                break;
        }
    }
    
    createRipple(element) {
        const ripple = document.createElement('div');
        ripple.className = 'ripple';
        element.appendChild(ripple);
        
        setTimeout(() => ripple.remove(), 300);
    }
    
    animateGlobe() {
        const globe = this.keyboard.querySelector('[data-action="globe"]');
        globe.style.transform = 'rotate(360deg)';
        globe.style.transition = 'transform 0.5s ease';
        
        setTimeout(() => {
            globe.style.transform = '';
            globe.style.transition = '';
        }, 500);
    }
    
    updateOutput() {
        this.outputText.textContent = this.typedText;
        this.textInput.value = this.typedText.replace(/\n/g, ' ');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new iOS26Keyboard();
});
