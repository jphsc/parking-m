import { AfterContentChecked, AfterContentInit, Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterContentChecked {

  protected tituloPagina: string = "";
  protected mostrarCabecalho = true;
  public title = 'ParkingM';

  constructor(private rota: Router, private titleService: Title) { }

  ngAfterContentChecked(): void {
    const url = this.rota.url;
    this.tituloPagina = this.getTituloPagina(url);
    this.mostrarCabecalho = !(url == '/pagina-nao-encontrada');
    // this.titleService.setTitle(this.mostrarCabecalho ? ` - ${this.tituloPagina}` : 'ParkingM - Página não encontrada');
    this.titleService.setTitle(this.mostrarCabecalho ? `${this.title} - ${this.tituloPagina}` : `${this.title} - Página não encontrada`);
  }

  private getTituloPagina(url: string): string {

    const regex = /^\/([^\/]+\/[^\/]+)/; //retorna todo o conteudo entre o primeiro e o terceiro '/'
    const match = url.match(regex)?.[0];
    let titulo = "";

    switch(match){
      case '/veiculo/cadastrar':
        titulo = "Cadastrar veículo";
        break;
      case '/veiculo/listar':
        titulo = "Listar veículos";
        break;
      case '/veiculo/editar':
        titulo = "Editar veículo";
        break;
      case '/veiculo/detalhe':
        titulo = "Detalhes do veículo";
        break;
      case '/movimento/cadastrar':
        titulo = "Gerar movimento de veículo";
        break;
      case '/movimento/listar':
        titulo = "Listar movimentos de veículos";
        break;
      case '/movimento/editar':
        titulo = "Encerrar movimento de veículo";
        break;
      case '/movimento/detalhe':
        titulo = "Detalhes do movimento do veículo";
        break;
      case '/movimento/finalizar':
        titulo = "Finalizar movimento de veículo";
        break;
      case '/regra/cadastrar':
        titulo = "Cadastrar regra financeira";
        break;
      case '/regra/listar':
        titulo = "Listar regras financeiras";
        break;
      case '/regra/editar':
        titulo = "Editar regra financeira";
        break;
      case '/regra/detalhe':
        titulo = "Detalhes da regra financeira";
        break;
      default:
        titulo = 'Home';
        break;
    }
  return titulo;
  }
}
