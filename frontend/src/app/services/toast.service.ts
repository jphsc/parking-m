import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  protected mensagem: string = '';
  private classToast!: string;
  private duracaoEmMs = 3000;
  private readonly labelBtn: string = 'Fechar';

  constructor(private snackBar: MatSnackBar) { }

  gerarToast(msg: string, isSuccess: boolean): void{
    this.mensagem = msg;
    this.classToast = isSuccess ? 'toast-sucesso' : 'toast-erro';
    this.duracaoEmMs = isSuccess ? this.duracaoEmMs : 10000;

    this.toast();
  }

  private toast(): void{
    this.snackBar.open(
      this.mensagem, this.labelBtn,
      {
        duration: this.duracaoEmMs,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: [this.classToast]
      }
    );
  }
}
