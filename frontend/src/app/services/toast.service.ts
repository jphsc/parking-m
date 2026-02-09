import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  protected mensagem: string = '';
  protected isSucesso!: boolean;
  private classToast!: string;
  private duracaoEmMs = 3000;
  private readonly labelBtn: string = 'Fechar';

  constructor(private snackBar: MatSnackBar) { }

  gerarToast(msg: string, isSuccess: boolean): void{
    this.mensagem = msg;
    this.isSucesso = isSuccess;
    this.classToast = this.isSucesso ? 'toast-sucesso' : 'toast-erro';
    this.duracaoEmMs = this.isSucesso ? 3000 : 10000;

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
