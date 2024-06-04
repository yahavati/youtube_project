import img1 from './img1.jpg';
import img2 from './img2.jpg';
import img3 from './img3.jpg';
import img4 from './img4.jpg';
import img5 from './img5.jpg';
import img6 from './img6.jpg';


const Videos = [
  { 
    id: 1, 
    title: 'Food', 
    author: 'Lian', 
    views: '120k', 
    when: '1 week ago', 
    img: img1, 
    videoUrl: 'https://rr5---sn-a5meknzs.googlevideo.com/videoplayback?expire=1717103161&ei=2ZVYZteXH7bjsfIPj7Ot8A4&ip=83.166.205.46&id=o-ALQenMDVFVdoBIIBbRDKG-7YJhV1LFdeCyEwgQInEHms&itag=18&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&bui=AWRWj2RqpvnrWQ8q64Rxxy_d-eAeQUuBmPw_Cx1WjqLGU3mEBWZOz4VmNAj730dnEtgyY4c8j0Brs-HR&vprv=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=9348215&ratebypass=yes&dur=223.515&lmt=1717069926604361&c=MEDIA_CONNECT_FRONTEND&txp=5538434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Cmime%2Crqh%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AJfQdSswRQIgXUka__H249K4nZT8qUyA1cUlf5ILEaDO_Li2eEId_uYCIQDvDUTit1gRHqnT8R8oP5o0_zteR0m1jDhDXvu0XU2zjg%3D%3D&title=%D7%94%D7%95%D7%95%D7%99%D7%96%D7%90%D7%A8%D7%93%D7%A1%20%D7%94%D7%95%D7%A4%D7%9B%D7%99%D7%9D%20%D7%90%D7%AA%20%D7%91%D7%A8%D7%99%D7%90%D7%9F%20%D7%A7%D7%99%D7%A3%20%D7%A8%D7%A9%D7%9E%D7%99%D7%AA%20%D7%9C%D7%9E%D7%90%D7%9E%D7%9F%20%D7%94%D7%A8%D7%90%D7%A9%D7%99%20%23%D7%9E%D7%A2%D7%A7%D7%91%D7%93%D7%99%D7%94&redirect_counter=1&cm2rm=sn-nx5sz7l&fexp=24350477&req_id=4d29e8994ba0a3ee&cms_redirect=yes&cmsv=e&mh=W0&mip=2a06:c701:4501:d100:793d:a3ad:7713:b91b&mm=34&mn=sn-a5meknzs&ms=ltu&mt=1717078750&mv=u&mvi=5&pl=29&lsparams=mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHWaYeowRQIhAN65udxNzRlAHOT_LYdLME9ymP_7a0TeTpGjtV-8SEJ8AiBN_ZFQnfVADf9Y8DYEJ_Q8zj0kkdMxlVihKlmZN3iPaw%3D%3D' 
  },
  { 
    id: 2, 
    title: 'News', 
    author: '13 רשת', 
    views: '20m', 
    when: '2 days ago', 
    img: img6, 
    videoUrl: 'https://rr4---sn-pujapa-ua8e.googlevideo.com/videoplayback?expire=1717104426&ei=yppYZp_oJqm0kucPpZWuiAs&ip=204.93.149.12&id=o-ALWYgOvDT4IybkE1_l7B3wOwsbuR5lTO3ZcX8QPO0My0&itag=18&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&bui=AWRWj2SpSNNK0yko7Fp0v5hvyMcAhARk8VuEup3QEPJIgTyyc4oxmURvkp3QZttJToXV11LhjXTGclWj&vprv=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=9092189&ratebypass=yes&dur=177.052&lmt=1713172601868986&c=MEDIA_CONNECT_FRONTEND&txp=5538434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Cmime%2Crqh%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AJfQdSswRQIhAKKbBTifKWFRYNz-5CygjiNX-Y4J2zovItBmMCG6udxjAiB3aoZjqLSdmNTD43jK5xo0Mkwt9DmKOmCbLu4DgATTPg%3D%3D&title=%D7%90%D7%97%D7%A8%D7%99%20%D7%94%D7%9C%D7%99%D7%9C%D7%94%20%D7%94%D7%93%D7%A8%D7%9E%D7%98%D7%99%2C%20%D7%94%D7%98%D7%99%D7%99%D7%A1%D7%99%D7%9D%20%D7%9E%D7%93%D7%91%D7%A8%D7%99%D7%9D%3A%20%22%D7%96%D7%94%20%D7%94%D7%99%D7%94%20%D7%A8%D7%92%D7%A2%20%D7%94%D7%90%D7%9E%D7%AA%22&redirect_counter=1&rm=sn-p5qeey7z&fexp=24350477&req_id=5dd195e7409da3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=ZF&mip=2a06:c701:4501:d100:793d:a3ad:7713:b91b&mm=31&mn=sn-pujapa-ua8e&ms=au&mt=1717078371&mv=u&mvi=4&pl=50&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHWaYeowRQIgDCZmXqNfT-6guid3eVaBYGvOV5lnhJoHQsuL78NC44ICIQCrsGU7FasJ_wF3Dlpm21Ll6OKZr1pI861nbGuGSXw5ag%3D%3D' 
  },
  { 
    id: 3, 
    title: 'Maccabi Tel Aviv', 
    author: 'Sport 5', 
    views: '10m', 
    when: '4 days ago', 
    img: img5, 
    videoUrl: 'https://rr4---sn-ua87sn76.googlevideo.com/videoplayback?expire=1717103440&ei=8JZYZtnyH9eMybgP7My6gAc&ip=181.41.206.102&id=o-AEUx3urZ3ZSKp4CYKLDoLIKAS7jU_UtZuyJyVzMbiyL_&itag=18&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&bui=AWRWj2SyXb1maniMyNEF2bj27UPpcjdA92CDNOfBBhobBHgwHTPFGMOrQgklI5ZNbNc4jKta2g0q6k-Q&vprv=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=20204208&ratebypass=yes&dur=237.586&lmt=1713916744552971&c=MEDIA_CONNECT_FRONTEND&txp=5538434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Cmime%2Crqh%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AJfQdSswRQIhAO4wPrPmdDsaSxN9xInDMiqWJOz4i7hpGJE2g05lvXULAiAsPG7C2v5ZJVgDkB9pvVqjRGXN8r4EYp-7qn4h1_7IQQ%3D%3D&title=Panathinaikos%20-%20Maccabi%20%7C%20PLAYOFFS%20GAME%201%20%7C%202023-24%20Turkish%20Airlines%20EuroLeague&rm=sn-vgqeee7s&fexp=24350477&req_id=3fbefc97ab17a3ee&ipbypass=yes&redirect_counter=2&cm2rm=sn-pujapa-ua8l7l&cms_redirect=yes&cmsv=e&mh=j2&mip=2a06:c701:4501:d100:793d:a3ad:7713:b91b&mm=29&mn=sn-ua87sn76&ms=rdu&mt=1717080541&mv=m&mvi=4&pl=50&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHWaYeowRQIgf24WDLtxHhPtTRQisNvPnsKuuTGjhZHHKjexBd08mvgCIQDjPe5KTgKagN5obtz4pzba5t6MDxsMB6uEqsPlh3sCuw%3D%3D' 
  },
  { 
    id: 4, 
    title: 'Music', 
    author: 'Alicia Keys', 
    views: '120k', 
    when: '2 weeks ago', 
    img: img4, 
    videoUrl: 'https://rr2---sn-pujapa-ua8l.googlevideo.com/videoplayback?expire=1717105313&ei=QZ5YZpzoHbOzkucPpKWJiAo&ip=154.16.192.73&id=o-AOOnWTIvrP5MA30QXXTShPr6EiHH0NWbdzvsQ5jhwUg9&itag=18&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&bui=AWRWj2RR6yt9whTi9r7wyZHJ3UslMOBpnR08NcJB-L0ByFzf5hTu9L_c526Zu5HZepMhvf0oyHszElf2&vprv=1&mime=video%2Fmp4&rqh=1&gir=yes&clen=20507496&ratebypass=yes&dur=250.241&lmt=1711261326244587&c=MEDIA_CONNECT_FRONTEND&txp=5538434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cvprv%2Cmime%2Crqh%2Cgir%2Cclen%2Cratebypass%2Cdur%2Clmt&sig=AJfQdSswRQIhAK8BFTFBh4O7z4S-P7b2VnLOVmh_LybZ6dlWPQL_6y_3AiAE7beGUZCxbbd5Rc2jO6HAwL-LN19TdOft7VIbT1a8ow%3D%3D&title=Alicia%20Keys%20-%20New%20York&redirect_counter=1&rm=sn-p5qeer76&fexp=24350477&req_id=22812ba152afa3ee&cms_redirect=yes&cmsv=e&ipbypass=yes&mh=sA&mip=2a06:c701:4501:d100:793d:a3ad:7713:b91b&mm=31&mn=sn-pujapa-ua8l&ms=au&mt=1717082960&mv=m&mvi=2&pl=50&lsparams=ipbypass,mh,mip,mm,mn,ms,mv,mvi,pl&lsig=AHWaYeowRAIgc6YjsJSo2tGcni1TfsZaFV8kFWbNO9LzDJfM_FOyIDQCIFO6iDzAvzuGXf0VxFKQa5ceb60493Et6z4DQVqwE_Sz' 
  },
  { 
    id: 5, 
    title: 'Food', 
    author: 'Lian', 
    views: '120k', 
    when: '1 week ago', 
    img: img1, 
    videoUrl: 'https://path-to-your-video-file/video5.mp4' 
  },
  { 
    id: 6, 
    title: 'News', 
    author: '13 רשת', 
    views: '20m', 
    when: '2 days ago', 
    img: img2, 
    videoUrl: 'https://path-to-your-video-file/video6.mp4' 
  },
  { 
    id: 7, 
    title: 'Maccabi Tel Aviv', 
    author: 'Sport 5', 
    views: '10m', 
    when: '4 days ago', 
    img: img3, 
    videoUrl: 'https://path-to-your-video-file/video7.mp4' 
  },
  { 
    id: 8, 
    title: 'Trips', 
    author: 'Yahav Atias', 
    views: '120k', 
    when: '2 weeks ago', 
    img: img4, 
    videoUrl: 'https://path-to-your-video-file/video8.mp4' 
  },
  { 
    id: 9, 
    title: 'Food', 
    author: 'Lian', 
    views: '120k', 
    when: '1 week ago', 
    img: img1, 
    videoUrl: 'https://path-to-your-video-file/video9.mp4' 
  },
  { 
    id: 10, 
    title: 'News', 
    author: '13 רשת', 
    views: '20m', 
    when: '2 days ago', 
    img: img2, 
    videoUrl: 'https://path-to-your-video-file/video10.mp4' 
  },
  { 
    id: 11, 
    title: 'Maccabi Tel Aviv', 
    author: 'Sport 5', 
    views: '10m', 
    when: '4 days ago', 
    img: img3, 
    videoUrl: 'https://path-to-your-video-file/video11.mp4' 
  },
  { 
    id: 12, 
    title: 'Trips', 
    author: 'Yahav Atias', 
    views: '120k', 
    when: '2 weeks ago', 
    img: img4, 
    videoUrl: 'https://path-to-your-video-file/video12.mp4' 
  }
];

export default Videos;
